import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { IEventContext } from 'app/entities/event-context/event-context.model';
import { EventContextService } from 'app/entities/event-context/service/event-context.service';
import { EventRegistrationStatus } from 'app/entities/enumerations/event-registration-status.model';
import { EventRegistrationService } from '../service/event-registration.service';
import { IEventRegistration } from '../event-registration.model';
import { EventRegistrationFormGroup, EventRegistrationFormService } from './event-registration-form.service';

@Component({
  selector: 'jhi-event-registration-update',
  templateUrl: './event-registration-update.component.html',
})
export class EventRegistrationUpdateComponent implements OnInit {
  isSaving = false;
  eventRegistration: IEventRegistration | null = null;
  eventRegistrationStatusValues = Object.keys(EventRegistrationStatus);

  applicationUsersSharedCollection: IApplicationUser[] = [];
  eventContextsSharedCollection: IEventContext[] = [];

  editForm: EventRegistrationFormGroup = this.eventRegistrationFormService.createEventRegistrationFormGroup();

  constructor(
    protected eventRegistrationService: EventRegistrationService,
    protected eventRegistrationFormService: EventRegistrationFormService,
    protected applicationUserService: ApplicationUserService,
    protected eventContextService: EventContextService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareEventContext = (o1: IEventContext | null, o2: IEventContext | null): boolean =>
    this.eventContextService.compareEventContext(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventRegistration }) => {
      this.eventRegistration = eventRegistration;
      if (eventRegistration) {
        this.updateForm(eventRegistration);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventRegistration = this.eventRegistrationFormService.getEventRegistration(this.editForm);
    if (eventRegistration.id !== null) {
      this.subscribeToSaveResponse(this.eventRegistrationService.update(eventRegistration));
    } else {
      this.subscribeToSaveResponse(this.eventRegistrationService.create(eventRegistration));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventRegistration>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(eventRegistration: IEventRegistration): void {
    this.eventRegistration = eventRegistration;
    this.eventRegistrationFormService.resetForm(this.editForm, eventRegistration);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      eventRegistration.eventCounterparty,
    );
    this.eventContextsSharedCollection = this.eventContextService.addEventContextToCollectionIfMissing<IEventContext>(
      this.eventContextsSharedCollection,
      eventRegistration.eventContext,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.eventRegistration?.eventCounterparty,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.eventContextService
      .query()
      .pipe(map((res: HttpResponse<IEventContext[]>) => res.body ?? []))
      .pipe(
        map((eventContexts: IEventContext[]) =>
          this.eventContextService.addEventContextToCollectionIfMissing<IEventContext>(eventContexts, this.eventRegistration?.eventContext),
        ),
      )
      .subscribe((eventContexts: IEventContext[]) => (this.eventContextsSharedCollection = eventContexts));
  }
}

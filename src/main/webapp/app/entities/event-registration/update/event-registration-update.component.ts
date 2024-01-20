import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EventRegistrationFormService, EventRegistrationFormGroup } from './event-registration-form.service';
import { IEventRegistration } from '../event-registration.model';
import { EventRegistrationService } from '../service/event-registration.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEventContext } from 'app/entities/event-context/event-context.model';
import { EventContextService } from 'app/entities/event-context/service/event-context.service';
import { EventRegistrationStatus } from 'app/entities/enumerations/event-registration-status.model';

@Component({
  selector: 'jhi-event-registration-update',
  templateUrl: './event-registration-update.component.html',
})
export class EventRegistrationUpdateComponent implements OnInit {
  isSaving = false;
  eventRegistration: IEventRegistration | null = null;
  eventRegistrationStatusValues = Object.keys(EventRegistrationStatus);

  usersSharedCollection: IUser[] = [];
  eventContextsSharedCollection: IEventContext[] = [];

  editForm: EventRegistrationFormGroup = this.eventRegistrationFormService.createEventRegistrationFormGroup();

  constructor(
    protected eventRegistrationService: EventRegistrationService,
    protected eventRegistrationFormService: EventRegistrationFormService,
    protected userService: UserService,
    protected eventContextService: EventContextService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

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

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      eventRegistration.eventCounterparty
    );
    this.eventContextsSharedCollection = this.eventContextService.addEventContextToCollectionIfMissing<IEventContext>(
      this.eventContextsSharedCollection,
      eventRegistration.eventContext
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.eventRegistration?.eventCounterparty)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.eventContextService
      .query()
      .pipe(map((res: HttpResponse<IEventContext[]>) => res.body ?? []))
      .pipe(
        map((eventContexts: IEventContext[]) =>
          this.eventContextService.addEventContextToCollectionIfMissing<IEventContext>(eventContexts, this.eventRegistration?.eventContext)
        )
      )
      .subscribe((eventContexts: IEventContext[]) => (this.eventContextsSharedCollection = eventContexts));
  }
}

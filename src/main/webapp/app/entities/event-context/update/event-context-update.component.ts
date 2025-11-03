import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { EventContextStatus } from 'app/entities/enumerations/event-context-status.model';
import { EventContextService } from '../service/event-context.service';
import { IEventContext } from '../event-context.model';
import { EventContextFormGroup, EventContextFormService } from './event-context-form.service';

@Component({
  selector: 'jhi-event-context-update',
  templateUrl: './event-context-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EventContextUpdateComponent implements OnInit {
  isSaving = false;
  eventContext: IEventContext | null = null;
  eventContextStatusValues = Object.keys(EventContextStatus);

  eventContextRoomsCollection: IRoom[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  eventsSharedCollection: IEvent[] = [];

  protected eventContextService = inject(EventContextService);
  protected eventContextFormService = inject(EventContextFormService);
  protected roomService = inject(RoomService);
  protected applicationUserService = inject(ApplicationUserService);
  protected eventService = inject(EventService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EventContextFormGroup = this.eventContextFormService.createEventContextFormGroup();

  compareRoom = (o1: IRoom | null, o2: IRoom | null): boolean => this.roomService.compareRoom(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareEvent = (o1: IEvent | null, o2: IEvent | null): boolean => this.eventService.compareEvent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventContext }) => {
      this.eventContext = eventContext;
      if (eventContext) {
        this.updateForm(eventContext);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventContext = this.eventContextFormService.getEventContext(this.editForm);
    if (eventContext.id !== null) {
      this.subscribeToSaveResponse(this.eventContextService.update(eventContext));
    } else {
      this.subscribeToSaveResponse(this.eventContextService.create(eventContext));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventContext>>): void {
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

  protected updateForm(eventContext: IEventContext): void {
    this.eventContext = eventContext;
    this.eventContextFormService.resetForm(this.editForm, eventContext);

    this.eventContextRoomsCollection = this.roomService.addRoomToCollectionIfMissing<IRoom>(
      this.eventContextRoomsCollection,
      eventContext.eventContextRoom,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      eventContext.contextHost,
    );
    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing<IEvent>(this.eventsSharedCollection, eventContext.event);
  }

  protected loadRelationshipsOptions(): void {
    this.roomService
      .query({ filter: 'eventcontext-is-null' })
      .pipe(map((res: HttpResponse<IRoom[]>) => res.body ?? []))
      .pipe(map((rooms: IRoom[]) => this.roomService.addRoomToCollectionIfMissing<IRoom>(rooms, this.eventContext?.eventContextRoom)))
      .subscribe((rooms: IRoom[]) => (this.eventContextRoomsCollection = rooms));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.eventContext?.contextHost,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing<IEvent>(events, this.eventContext?.event)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RoomFormService, RoomFormGroup } from './room-form.service';
import { IRoom } from '../room.model';
import { RoomService } from '../service/room.service';
import { IEventContext } from 'app/entities/event-context/event-context.model';
import { EventContextService } from 'app/entities/event-context/service/event-context.service';

@Component({
  selector: 'jhi-room-update',
  templateUrl: './room-update.component.html',
})
export class RoomUpdateComponent implements OnInit {
  isSaving = false;
  room: IRoom | null = null;

  eventContextsSharedCollection: IEventContext[] = [];

  editForm: RoomFormGroup = this.roomFormService.createRoomFormGroup();

  constructor(
    protected roomService: RoomService,
    protected roomFormService: RoomFormService,
    protected eventContextService: EventContextService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEventContext = (o1: IEventContext | null, o2: IEventContext | null): boolean =>
    this.eventContextService.compareEventContext(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ room }) => {
      this.room = room;
      if (room) {
        this.updateForm(room);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const room = this.roomFormService.getRoom(this.editForm);
    if (room.id !== null) {
      this.subscribeToSaveResponse(this.roomService.update(room));
    } else {
      this.subscribeToSaveResponse(this.roomService.create(room));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoom>>): void {
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

  protected updateForm(room: IRoom): void {
    this.room = room;
    this.roomFormService.resetForm(this.editForm, room);

    this.eventContextsSharedCollection = this.eventContextService.addEventContextToCollectionIfMissing<IEventContext>(
      this.eventContextsSharedCollection,
      room.roomEventContext
    );
  }

  protected loadRelationshipsOptions(): void {
    this.eventContextService
      .query()
      .pipe(map((res: HttpResponse<IEventContext[]>) => res.body ?? []))
      .pipe(
        map((eventContexts: IEventContext[]) =>
          this.eventContextService.addEventContextToCollectionIfMissing<IEventContext>(eventContexts, this.room?.roomEventContext)
        )
      )
      .subscribe((eventContexts: IEventContext[]) => (this.eventContextsSharedCollection = eventContexts));
  }
}

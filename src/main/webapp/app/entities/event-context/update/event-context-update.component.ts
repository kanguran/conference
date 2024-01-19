import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EventContextFormService, EventContextFormGroup } from './event-context-form.service';
import { IEventContext } from '../event-context.model';
import { EventContextService } from '../service/event-context.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { EventContextStatus } from 'app/entities/enumerations/event-context-status.model';

@Component({
  selector: 'jhi-event-context-update',
  templateUrl: './event-context-update.component.html',
})
export class EventContextUpdateComponent implements OnInit {
  isSaving = false;
  eventContext: IEventContext | null = null;
  eventContextStatusValues = Object.keys(EventContextStatus);

  eventsSharedCollection: IEvent[] = [];

  editForm: EventContextFormGroup = this.eventContextFormService.createEventContextFormGroup();

  constructor(
    protected eventContextService: EventContextService,
    protected eventContextFormService: EventContextFormService,
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute
  ) {}

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

    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing<IEvent>(this.eventsSharedCollection, eventContext.event);
  }

  protected loadRelationshipsOptions(): void {
    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing<IEvent>(events, this.eventContext?.event)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));
  }
}

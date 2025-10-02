import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventContext } from '../event-context.model';

@Component({
  selector: 'jhi-event-context-detail',
  templateUrl: './event-context-detail.component.html',
})
export class EventContextDetailComponent implements OnInit {
  eventContext: IEventContext | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventContext }) => {
      this.eventContext = eventContext;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

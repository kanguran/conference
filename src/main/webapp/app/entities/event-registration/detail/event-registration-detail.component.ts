import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventRegistration } from '../event-registration.model';

@Component({
  selector: 'jhi-event-registration-detail',
  templateUrl: './event-registration-detail.component.html',
})
export class EventRegistrationDetailComponent implements OnInit {
  eventRegistration: IEventRegistration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventRegistration }) => {
      this.eventRegistration = eventRegistration;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

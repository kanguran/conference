import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IEventRegistration } from '../event-registration.model';

@Component({
  selector: 'jhi-event-registration-detail',
  templateUrl: './event-registration-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class EventRegistrationDetailComponent {
  eventRegistration = input<IEventRegistration | null>(null);

  previousState(): void {
    window.history.back();
  }
}

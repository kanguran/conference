import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IEvent } from '../event.model';

@Component({
  selector: 'jhi-event-detail',
  templateUrl: './event-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class EventDetailComponent {
  event = input<IEvent | null>(null);

  previousState(): void {
    window.history.back();
  }
}

import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IEventContext } from '../event-context.model';

@Component({
  selector: 'jhi-event-context-detail',
  templateUrl: './event-context-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class EventContextDetailComponent {
  eventContext = input<IEventContext | null>(null);

  previousState(): void {
    window.history.back();
  }
}

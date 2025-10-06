import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEventRegistration } from '../event-registration.model';
import { EventRegistrationService } from '../service/event-registration.service';

@Component({
  templateUrl: './event-registration-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EventRegistrationDeleteDialogComponent {
  eventRegistration?: IEventRegistration;

  protected eventRegistrationService = inject(EventRegistrationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventRegistrationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

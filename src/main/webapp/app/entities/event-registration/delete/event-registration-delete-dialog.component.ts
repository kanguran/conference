import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventRegistration } from '../event-registration.model';
import { EventRegistrationService } from '../service/event-registration.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './event-registration-delete-dialog.component.html',
})
export class EventRegistrationDeleteDialogComponent {
  eventRegistration?: IEventRegistration;

  constructor(protected eventRegistrationService: EventRegistrationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventRegistrationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApplicationUser } from '../application-user.model';
import { ApplicationUserService } from '../service/application-user.service';

@Component({
  templateUrl: './application-user-delete-dialog.component.html',
})
export class ApplicationUserDeleteDialogComponent {
  applicationUser?: IApplicationUser;

  constructor(
    protected applicationUserService: ApplicationUserService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.applicationUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

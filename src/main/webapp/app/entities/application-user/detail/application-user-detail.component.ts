import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IApplicationUser } from '../application-user.model';

@Component({
  selector: 'jhi-application-user-detail',
  templateUrl: './application-user-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ApplicationUserDetailComponent {
  applicationUser = input<IApplicationUser | null>(null);

  previousState(): void {
    window.history.back();
  }
}

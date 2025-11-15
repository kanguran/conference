import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EventRegistrationDetailComponent } from './event-registration-detail.component';

describe('EventRegistration Management Detail Component', () => {
  let comp: EventRegistrationDetailComponent;
  let fixture: ComponentFixture<EventRegistrationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventRegistrationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./event-registration-detail.component').then(m => m.EventRegistrationDetailComponent),
              resolve: { eventRegistration: () => of({ id: 3555 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EventRegistrationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EventRegistrationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load eventRegistration on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EventRegistrationDetailComponent);

      // THEN
      expect(instance.eventRegistration()).toEqual(expect.objectContaining({ id: 3555 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});

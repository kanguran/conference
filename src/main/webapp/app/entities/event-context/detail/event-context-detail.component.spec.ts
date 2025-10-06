import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EventContextDetailComponent } from './event-context-detail.component';

describe('EventContext Management Detail Component', () => {
  let comp: EventContextDetailComponent;
  let fixture: ComponentFixture<EventContextDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventContextDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./event-context-detail.component').then(m => m.EventContextDetailComponent),
              resolve: { eventContext: () => of({ id: 18286 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EventContextDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EventContextDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load eventContext on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EventContextDetailComponent);

      // THEN
      expect(instance.eventContext()).toEqual(expect.objectContaining({ id: 18286 }));
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

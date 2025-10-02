import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventRegistrationDetailComponent } from './event-registration-detail.component';

describe('EventRegistration Management Detail Component', () => {
  let comp: EventRegistrationDetailComponent;
  let fixture: ComponentFixture<EventRegistrationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventRegistrationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eventRegistration: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EventRegistrationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EventRegistrationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eventRegistration on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eventRegistration).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

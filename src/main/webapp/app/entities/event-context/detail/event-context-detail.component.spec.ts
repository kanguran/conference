import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventContextDetailComponent } from './event-context-detail.component';

describe('EventContext Management Detail Component', () => {
  let comp: EventContextDetailComponent;
  let fixture: ComponentFixture<EventContextDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventContextDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eventContext: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EventContextDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EventContextDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eventContext on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eventContext).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

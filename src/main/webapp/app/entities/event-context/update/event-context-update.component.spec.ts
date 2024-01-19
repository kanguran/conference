import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EventContextFormService } from './event-context-form.service';
import { EventContextService } from '../service/event-context.service';
import { IEventContext } from '../event-context.model';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

import { EventContextUpdateComponent } from './event-context-update.component';

describe('EventContext Management Update Component', () => {
  let comp: EventContextUpdateComponent;
  let fixture: ComponentFixture<EventContextUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventContextFormService: EventContextFormService;
  let eventContextService: EventContextService;
  let eventService: EventService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EventContextUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EventContextUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventContextUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventContextFormService = TestBed.inject(EventContextFormService);
    eventContextService = TestBed.inject(EventContextService);
    eventService = TestBed.inject(EventService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Event query and add missing value', () => {
      const eventContext: IEventContext = { id: 456 };
      const event: IEvent = { id: 49686 };
      eventContext.event = event;

      const eventCollection: IEvent[] = [{ id: 57172 }];
      jest.spyOn(eventService, 'query').mockReturnValue(of(new HttpResponse({ body: eventCollection })));
      const additionalEvents = [event];
      const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
      jest.spyOn(eventService, 'addEventToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventContext });
      comp.ngOnInit();

      expect(eventService.query).toHaveBeenCalled();
      expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(
        eventCollection,
        ...additionalEvents.map(expect.objectContaining)
      );
      expect(comp.eventsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eventContext: IEventContext = { id: 456 };
      const event: IEvent = { id: 79512 };
      eventContext.event = event;

      activatedRoute.data = of({ eventContext });
      comp.ngOnInit();

      expect(comp.eventsSharedCollection).toContain(event);
      expect(comp.eventContext).toEqual(eventContext);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventContext>>();
      const eventContext = { id: 123 };
      jest.spyOn(eventContextFormService, 'getEventContext').mockReturnValue(eventContext);
      jest.spyOn(eventContextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventContext });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventContext }));
      saveSubject.complete();

      // THEN
      expect(eventContextFormService.getEventContext).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventContextService.update).toHaveBeenCalledWith(expect.objectContaining(eventContext));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventContext>>();
      const eventContext = { id: 123 };
      jest.spyOn(eventContextFormService, 'getEventContext').mockReturnValue({ id: null });
      jest.spyOn(eventContextService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventContext: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventContext }));
      saveSubject.complete();

      // THEN
      expect(eventContextFormService.getEventContext).toHaveBeenCalled();
      expect(eventContextService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventContext>>();
      const eventContext = { id: 123 };
      jest.spyOn(eventContextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventContext });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventContextService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEvent', () => {
      it('Should forward to eventService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(eventService, 'compareEvent');
        comp.compareEvent(entity, entity2);
        expect(eventService.compareEvent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

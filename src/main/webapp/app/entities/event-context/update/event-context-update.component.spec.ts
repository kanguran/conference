import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { IEventContext } from '../event-context.model';
import { EventContextService } from '../service/event-context.service';
import { EventContextFormService } from './event-context-form.service';

import { EventContextUpdateComponent } from './event-context-update.component';

describe('EventContext Management Update Component', () => {
  let comp: EventContextUpdateComponent;
  let fixture: ComponentFixture<EventContextUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventContextFormService: EventContextFormService;
  let eventContextService: EventContextService;
  let roomService: RoomService;
  let applicationUserService: ApplicationUserService;
  let eventService: EventService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EventContextUpdateComponent],
      providers: [
        provideHttpClient(),
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
    roomService = TestBed.inject(RoomService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    eventService = TestBed.inject(EventService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call eventContextRoom query and add missing value', () => {
      const eventContext: IEventContext = { id: 3213 };
      const eventContextRoom: IRoom = { id: 31469 };
      eventContext.eventContextRoom = eventContextRoom;

      const eventContextRoomCollection: IRoom[] = [{ id: 31469 }];
      jest.spyOn(roomService, 'query').mockReturnValue(of(new HttpResponse({ body: eventContextRoomCollection })));
      const expectedCollection: IRoom[] = [eventContextRoom, ...eventContextRoomCollection];
      jest.spyOn(roomService, 'addRoomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventContext });
      comp.ngOnInit();

      expect(roomService.query).toHaveBeenCalled();
      expect(roomService.addRoomToCollectionIfMissing).toHaveBeenCalledWith(eventContextRoomCollection, eventContextRoom);
      expect(comp.eventContextRoomsCollection).toEqual(expectedCollection);
    });

    it('should call ApplicationUser query and add missing value', () => {
      const eventContext: IEventContext = { id: 3213 };
      const contextHost: IApplicationUser = { id: 2107 };
      eventContext.contextHost = contextHost;

      const applicationUserCollection: IApplicationUser[] = [{ id: 2107 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [contextHost];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventContext });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('should call Event query and add missing value', () => {
      const eventContext: IEventContext = { id: 3213 };
      const event: IEvent = { id: 22576 };
      eventContext.event = event;

      const eventCollection: IEvent[] = [{ id: 22576 }];
      jest.spyOn(eventService, 'query').mockReturnValue(of(new HttpResponse({ body: eventCollection })));
      const additionalEvents = [event];
      const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
      jest.spyOn(eventService, 'addEventToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventContext });
      comp.ngOnInit();

      expect(eventService.query).toHaveBeenCalled();
      expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(
        eventCollection,
        ...additionalEvents.map(expect.objectContaining),
      );
      expect(comp.eventsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const eventContext: IEventContext = { id: 3213 };
      const eventContextRoom: IRoom = { id: 31469 };
      eventContext.eventContextRoom = eventContextRoom;
      const contextHost: IApplicationUser = { id: 2107 };
      eventContext.contextHost = contextHost;
      const event: IEvent = { id: 22576 };
      eventContext.event = event;

      activatedRoute.data = of({ eventContext });
      comp.ngOnInit();

      expect(comp.eventContextRoomsCollection).toContainEqual(eventContextRoom);
      expect(comp.applicationUsersSharedCollection).toContainEqual(contextHost);
      expect(comp.eventsSharedCollection).toContainEqual(event);
      expect(comp.eventContext).toEqual(eventContext);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventContext>>();
      const eventContext = { id: 18286 };
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

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventContext>>();
      const eventContext = { id: 18286 };
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

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventContext>>();
      const eventContext = { id: 18286 };
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
    describe('compareRoom', () => {
      it('should forward to roomService', () => {
        const entity = { id: 31469 };
        const entity2 = { id: 22394 };
        jest.spyOn(roomService, 'compareRoom');
        comp.compareRoom(entity, entity2);
        expect(roomService.compareRoom).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareApplicationUser', () => {
      it('should forward to applicationUserService', () => {
        const entity = { id: 2107 };
        const entity2 = { id: 4268 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEvent', () => {
      it('should forward to eventService', () => {
        const entity = { id: 22576 };
        const entity2 = { id: 3268 };
        jest.spyOn(eventService, 'compareEvent');
        comp.compareEvent(entity, entity2);
        expect(eventService.compareEvent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

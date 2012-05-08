package tv.esporx.controllers;



public class TimeLineControllerTest {

	// @Test
	// @SuppressWarnings("unchecked")
	//	public void when_all_event_casts_within_a_time_window_then_multimap_size_equals_number_of_events() {
	//		EventRepository eventDao = mock(EventRepository.class);
	//		TimeLineController timeLineController = new TimeLineController();
	//		timeLineController.setEventRepository(eventDao);
	//		Event event = new Event();
	//		event.setStartDate(new Date());
	//		event.setEndDate(new DateTime().plusDays(3).toDate());
	//		Cast cast = new Cast();
	//		cast.setBroadcastDate(new DateTime().plusMinutes(30).toDate());
	//		Cast cast2 = new Cast();
	//		cast2.setBroadcastDate(new DateTime().plusMinutes(40).toDate());
	//		Cast cast3 = new Cast();
	//		cast3.setBroadcastDate(new DateTime().plusMinutes(50).toDate());
	//		Cast cast4 = new Cast();
	//		cast4.setBroadcastDate(new DateTime().plusMinutes(60).toDate());
	//
	//		event.addCast(cast);
	//		event.addCast(cast2);
	//		event.addCast(cast3);
	//		event.addCast(cast4);
	//
	//		when(eventDao.findTimeLine(Mockito.any(DateTime.class), Mockito.any(DateTime.class))).thenReturn(singletonList(event));
	//
	//		Assertions.assertThat(((List<Event>) timeLineController.index(null).getModel().get("events")).size()).isEqualTo(0);
	//	}
}

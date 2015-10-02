package com.jayway.waytravel.dto;

import java.util.List;

/**
 * Created by carlemil on 2015-10-02.
 *
 * [
 {
 "title": "Going out for a beer",
 "description": "Örenäs is know for it's famous pubs and local breweries. Let's go out and drink some beers.",
 "participants": [
 {
 "name": "Mike",
 "id": "0"
 }
 ]
 }
 ]
 */
public class EventDTO {
    public String title;
    public String description;
    public List<ParticipantDTO> participants;
}

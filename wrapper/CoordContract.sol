pragma solidity ^0.4.0;
contract CoordContract {
    
    struct Event {
        string title;
        string description;
        uint128 latitude;
        uint128 longitud;
    }
    
    Event[] public listEvents;
    
    function addEvent(string t, string d, uint128 lat, uint128 long) public {
        listEvents.push(Event(t,d,lat,long));
    }
    
    function getEventsCount() public constant returns(uint) {
        return listEvents.length;
    }
    
    function getEventByIndex(uint index) public constant returns(string, string, uint128, uint128) {
        return (listEvents[index].title ,
                listEvents[index].description,
                listEvents[index].latitude,
                listEvents[index].longitud);
    }
}
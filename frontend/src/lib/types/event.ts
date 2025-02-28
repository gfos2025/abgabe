type EventId = number | string;
export interface Event {
    id: EventId;
    start: string;
    end: string;
    title?: string;
    people?: string[];
    location?: string;
    description?: string;
    calendarId?: string;
    _customContent?: {
        timeGrid?: string;
        dateGrid?: string;
        monthGrid?: string;
        monthAgenda?: string;
    }
};
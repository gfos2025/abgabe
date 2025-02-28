export interface Participant {
    id: string;
    name: string;
    role: 'ORGANIZER' | 'PARTICIPANT';
  }
export interface Appointment {
    id: string;
    startTime: string;
    endTime: string;
    description: string;
    participants: Participant[];
  }
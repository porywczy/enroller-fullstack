<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                //lista spotkan
                meetings: [],
                isError: false
            };
        },
        methods: {
            addNewMeeting(meeting) {
                //dodanie do listy (na koniec)
                this.meetings.push(meeting);

                this.$http.post('meetings', meeting)
                    .then(() => {
                        this.success('Spotkanie zostało dodane. Możesz się zapisać.');
                    })
                    .catch(response => this.failure('Błąd przy dodawaniu spotkania. Kod odpowiedzi: ' + response.status));


            },
            addMeetingParticipant(meeting) {
                meeting.participants.push(this.username);
            },
            removeMeetingParticipant(meeting) {
                meeting.participants.splice(meeting.participants.indexOf(this.username), 1);
            },
            deleteMeeting(meetingId) {
                //-1
                // If negative, it will begin that many elements from the end of the array
                // -n == array.length - n
                this.meetings.splice(meetingId, 1);

                this.$http.delete('meetings/'+String(Number(meetingId)+1))
                    .then(() => {
                        this.success('Spotkanie zostało usunięte.');
                    })
                    .catch(response => this.failure('Błąd przy usuwaniu spotkania. Kod odpowiedzi: ' + response.status));

            },
            success(message) {
                this.message = message;
                this.isError = false;
            },
            failure(message) {
                this.message = message;
                this.isError = true;
            }
        },
        // mounted() {
        //         this.$http.get(`meetings`);
        // },
    }
</script>

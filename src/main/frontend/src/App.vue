<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo">
      System do zapisów na zajęcia
    </h1>
    <div v-if="authenticatedUsername">
      <h2>Witaj {{ authenticatedUsername }}!
        <a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
      </h2>
      <meetings-page :username="authenticatedUsername"></meetings-page>
    </div>
    <div v-else>
      <button @click="registering = false">Zaloguj się</button>
      <button @click="registering = true">Zarejestruj się</button>
      <login-form @login="login($event)"
                  v-if="!registering"></login-form>
      <login-form @login="register($event)"
                  button-label="Zarejestruj się"
                  v-else></login-form>
    </div>
  </div>
</template>

<script>
    import "milligram";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";

    //hash jest w jedna strone
    //admin tez widzi tylko hashe
    export default {
        components: {LoginForm, MeetingsPage},
        data() {
            return {
                authenticatedUsername: "",
                registering: false
            };
        },
        methods: {
            login(user) {
                this.authenticatedUsername = user.login;
            },
            logout() {
                this.authenticatedUsername = '';
            },
            //usluga wysyla do backendu
            register(user) {
                this.$http.post('participants', user)
                    .then(response => {//kiedy serwer wykona
                    // udało się
                })
                    .catch(response => {
                            // nie udało sie
                            alert("User już istnieje")
                        });
            }
        }
    };
</script>

<style>
  #app {
    max-width: 1000px;
    margin: 0 auto;
  }

  .logo {
    vertical-align: middle;
  }
</style>


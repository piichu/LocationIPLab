<script>
import axios from 'axios'

export default {
  data() {
    return {
      address: "",
      error: "",
      info: null
    }
  },
  computed: {
    showCountry() {
      return "Cтрана: " + this.info.country
    },
    showCity() {
      return "Город: " + this.info.city
    },
    showTags() {
      let list = "Тэги: "
      for (let tag of this.info.tags) {
        list += tag.name + ' '
      }
      return list
    },
    showAddresses() {
      let list = "Адреса: "
      for (let ip of this.info.ips) {
        list += ip.address + ' '
      }
      return list
    }
  },
  methods: {
    getLocation() {
      axios.get(`http://localhost:8080/locations/ip?address=${this.address}`).then(res => {
        if (res.data == "") {
          this.info = null
          this.error = "Местоположение не найдено"
        } else {
          this.info = res.data
          this.error = ""
        }
      })
    },
    goToTags() {
      this.$router.push('/tags')
    },
    goToEdit() {
      this.$router.push('/edit')
    }
  }

}
</script>

<template>
  <button class="top-right" @click="goToTags">Тэги</button>
  <button class="aaa" @click="goToEdit"></button>
  <div class="wrapper">
    <div class="planet">
      <img src="../assets/planet-earth-global-svgrepo-com.svg" alt="Planet Image">
    </div>
    <div class="content">
      <h1>Узнать местоположение по IP</h1>
      <input type="text" v-model="address" placeholder="IP адрес">
      <button v-show="address != ''" @click="getLocation()">Показать</button>
      <p class="error">{{ error }}</p>
      <div class="location" v-if="info != null">
        <p>{{ showCountry }}</p>
        <p>{{ showCity }}</p>
        <p>{{ showAddresses }}</p>
        <p>{{ showTags }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.wrapper {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  max-width: 1366px;
  text-align: center;
}

.planet {
  width: 650px;
  height: 700px;
  overflow: hidden;
  position: relative;
  margin-right: 20px;
}

.planet img {
  width: 800px;
  height: 800px;
  position: absolute;
  left: -150px;
  top: -100px;
  filter: invert(100);
}

.content {
  flex: 1 1 auto;
  padding: 20px;
  justify-content: center;
}

h1 {
  font-size: 42px;
  margin-bottom: 20px;
  font-family: 'VAG World Bold', Arial;
  background: linear-gradient(to right, #ffffff, #ffffff); /* Replace with your desired gradient colors */
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

input[type="text"] {
  width: 75%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 10px 20px;
  background-color: #5f4b8b;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #655094;
}

.error {
  color: #142738;
  font-size: 20px;
  font-family: 'VAG World Bold', sans-serif;
  margin-top: 10px;
  margin-bottom: 10px;
}

.location {
  margin-top: 20px;
  padding: 10px;
  background-color: #f2f2f2;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.location p {
  margin-bottom: 5px;
}

.top-right {
  position: fixed;
  top: 20px;
  right: 20px;
}

.aaa {
  position: fixed;
  bottom: 0;
  right: 0;
  width: 40px;
  height: 40px;
  background: transparent;
  border: none;
  outline: none;
  cursor: pointer;
  opacity: 0;
}
</style>

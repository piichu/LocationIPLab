<template>
  <div class="content">
    <h1>Места по признакам</h1>
    <input type="text" v-model="name" placeholder="Признак">
    <button v-show="name != ''" @click="getLocation()">Показать</button>
    <p class="error">{{ error }}</p>
    <ul class="list">
      <li v-for="location in info" :key="location.id">{{ location.country }} - {{ location.city }}</li>
    </ul>
  </div>
  <button class="top-right" @click="goToIps">IP</button>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      name: "",
      error: "",
      info: null
    }
  },
  methods: {
    getLocation() {
      axios.get(`http://localhost:8080/locations/tag?name=${this.name}`).then(res => {
        if (res.data == "") {
          this.info = null
          this.error = "Такие места не найдены"
        } else {
          this.info = res.data
          this.error = ""
        }
      })
    },
    goToIps(){
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.content {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
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


.top-right{
  position: fixed;
  top: 20px;
  right: 20px;
}

.list {
  list-style-type: none;
  padding: 0;
}

.list li {
  margin-bottom: 10px;
  padding: 10px;
  background-color: #f2f2f2;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
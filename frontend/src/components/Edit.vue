<template>
  <div class="container">
    <h1>Местоположения</h1>
    <form @submit.prevent="addNewLocation">
      <input v-model="newLocation.country" placeholder="Страна" required type="text"/>
      <input v-model="newLocation.city" placeholder="Город" required type="text"/>
      <h3>Тэги</h3>
      <div v-for="tag in tags" :key="tag.id">
        <input
            :id="tag.id"
            v-model="newLocation.tags"
            :value="tag.id"
            type="checkbox"
        />
        <label :for="tag.id">{{ tag.name }}</label>
        <span class="edit-icon" @click="renameTag(tag.id)">&#9998;</span>
        <span class="delete-icon" @click="deleteTag(tag.id)"> x</span>
        <input
            v-if="editingTag === tag.id"
            v-model="editingTagName"
            type="text"
            @blur="cancelEditing"
            @keydown.enter.prevent="updateTagName"
        />
      </div>
      <button type="submit">Добавить</button>
    </form>
    <h1>Добавить тэг</h1>
    <form @submit.prevent="addNewTag">
      <input v-model="newTag.name" placeholder="Имя тега" required type="text"/>
      <button type="submit">Добавить</button>
    </form>
    <ul class="location-list">
      <li v-for="location in locations" :key="location.id" class="location-item">
        <div class="location-item-content">
        <span>{{ location.country }} <span class="edit-icon"
                                           @click="renameCountry(location.id)">&#9998;</span>, {{ location.city }}<span
            class="edit-icon" @click="renameCity(location.id)">&#9998;</span></span>
          <input
              v-if="editingCountry === location.id"
              v-model="editingCountryName"
              type="text"
              @blur="cancelEditing"
              @keydown.enter.prevent="updateCountryName(location.id)"
          />
          <input
              v-if="editingCity === location.id"
              v-model="editingCityName"
              type="text"
              @blur="cancelEditing"
              @keydown.enter.prevent="updateCityName(location.id)"
          />
          <span v-if="location.tags.length>0">Тэги: </span>
          <span v-for="tag in location.tags" :key="tag.id">{{ tag.name }}
          <span class="delete-icon" @click="deleteTagFrom(location.id, tag.id)">x</span>
        </span>
          <span v-if="location.ips.length>0">Адреса: </span>
          <span v-for="ip in location.ips" :key="ip.id">{{ ip.address }}
          <span class="delete-icon" @click="deleteIp(ip.id)">x</span>
        </span>
          <button @click="deleteLocation(location.id)">Удалить</button>
          <button class="add-ip-button" @click="addIpToggle(location.id)">Добавить IP</button>
          <input
              v-if="addingIp === location.id"
              v-model="addingIpAddress"
              type="text"
              @blur="cancelEditing"
              @keydown.enter.prevent="addIp(location.id)"
          />
          <button class="add-ip-button" @click="toggleTagDropdown(location.id)">Добавить тег
          </button>
          <select v-if="addingTag===location.id" v-model="addingTagName">
            <option v-for="tag in tags" :key="tag.id" :value="tag.id">{{ tag.name }}</option>
          </select>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      locations: [],
      tags: [],
      editingTag: Number,
      editingTagName: '',
      editingCountry: Number,
      editingCountryName: '',
      editingCity: Number,
      editingCityName: '',
      addingIp: Number,
      addingIpAddress: '',
      addingTag: Number,
      addingTagName: '',
      newLocation: {
        country: '',
        city: '',
        tags: [],
        ips: [],
        showIpInput: false,
        showTagDropdown: false,
        address: '',
        selectedTag: Number
      },
      newTag: {
        name: ""
      }
    };
  },
  mounted() {
    this.fetchLocations()
    this.fetchTags();
  },
  methods: {
    async fetchLocations() {
      try {
        const response = await axios.get('http://localhost:8080/locations');
        this.locations = response.data.map(location => {
          return {...location, showIpInput: false, showTagDropdown: false}
        });
      } catch (error) {
        console.error(error);
      }
    },
    async fetchTags() {
      try {
        const response = await axios.get('http://localhost:8080/tags');
        this.tags = response.data;
      } catch (error) {
        console.error(error);
      }
    },
    async addNewLocation() {
      try {
        await axios.post(`http://localhost:8080/locations?country=${this.newLocation.country}&city=${this.newLocation.city}`, this.newLocation.tags);
        this.newLocation.country = '';
        this.newLocation.city = '';
        this.newLocation.tags = [];
        this.fetchLocations();
      } catch (error) {
        console.error(error);
      }
    },
    async deleteLocation(locationId) {
      try {
        await axios.delete(`http://localhost:8080/locations/${locationId}`);
        this.fetchLocations();
      } catch (error) {
        console.error(error);
      }
    },
    async addIp(locationId) {
      await axios.post(`http://localhost:8080/ips?locationId=${locationId}&address=${this.addingIpAddress}`)
      this.addingIp = null
      this.addingIpAddress = ''
      this.fetchLocations()
    },
    async deleteIp(ipId) {
      await axios.delete(`http://localhost:8080/ips/${ipId}`)
      this.fetchLocations()
    },
    async addNewTag() {
      try {
        await axios.post(`http://localhost:8080/tags?name=${this.newTag.name}`);
        this.newTag.name = '';
        this.fetchTags();
      } catch (error) {
        console.error(error);
      }
    },
    async deleteTag(tagId) {
      try {
        await axios.delete(`http://localhost:8080/tags/${tagId}`);
        this.fetchTags();
        this.fetchLocations();
      } catch (error) {
        console.error(error);
      }
    },
    async deleteTagFrom(locationId, tagId) {
      await axios.patch(`http://localhost:8080/locations/removeTag?locationId=${locationId}&tagId=${tagId}`)
      this.fetchLocations()
    },
    addIpToggle(locationId) {
      this.addingIp = locationId;
    },
    toggleTagDropdown(locationId) {
      if (this.addingTag !== locationId) {
        this.addingTag = locationId
      }else{
        this.addTag(locationId)
      }
    },
    async addTag(locationId) {
      await axios.patch(`http://localhost:8080/locations/addTag?locationId=${locationId}&tagId=${this.addingTagName}`)
      this.addingTag = null
      this.addingTagName = '';
      this.fetchLocations()
    },
    renameTag(tagId) {
      this.editingTag = tagId;
    },
    cancelEditing() {
      this.editingTag = null;
      this.editingTagName = '';
      this.editingCountry = null;
      this.editingCountryName = '';
      this.editingCity = null;
      this.editingCityName = '';
      this.addingIp = null;
      this.addingIpAddress = ''
      this.addingTag = null
      this.addingTagName = '';
    },
    async updateTagName() {
      await axios.put(`http://localhost:8080/tags/${this.editingTag}?name=${this.editingTagName}`);
      this.editingTag = null;
      this.editingTagName = '';
      this.fetchTags();
      this.fetchLocations();
    },
    renameCountry(locationId) {
      this.editingCountry = locationId;
    },
    renameCity(locationId) {
      this.editingCity = locationId;
    },
    async updateCountryName(locationId) {
      await axios.put(`http://localhost:8080/locations/${locationId}?country=${this.editingCountryName}`)
      this.editingCountry = null;
      this.editingCountryName = '';
      this.fetchLocations();
    },
    async updateCityName(locationId) {
      await axios.put(`http://localhost:8080/locations/${locationId}?city=${this.editingCityName}`)
      this.editingCity = null;
      this.editingCityName = '';
      this.fetchLocations();
    }

  }
};
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  font-size: 24px;
  margin-bottom: 20px;
}

form {
  margin-bottom: 20px;
}

input[type="text"] {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button[type="submit"] {
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 20px;
}

.edit-icon,
.delete-icon {
  display: inline-block;
  margin-left: 5px;
  cursor: pointer;
}

input.editing {
  border: 1px solid #007bff;
}

.delete-icon {
  color: #dc3545;
}

select {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 10px 20px;
  margin-right: 10px;
  background-color: #dc3545;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-ip-button {
  background-color: #17a2b8;
}

span {
  margin-right: 5px;
}

.location-list {
  display: flex;
  flex-wrap: wrap;
}

.location-item {
  flex: 0 0 33.33%;
  margin-bottom: 20px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.location-item-content {
  margin-bottom: 10px;
}

.location-item span {
  display: flex;
  margin-bottom: 5px;
}

.location-item button {
  margin-left: 10px;
}
</style>
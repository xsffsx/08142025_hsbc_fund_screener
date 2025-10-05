export default {
  template: `
    <div class="container">
      <div class="header">
          <h2>Compare products</h2>
      </div>
      <div class="upload-content">
          <div >
              <h3>product1</h3>
              <textarea rows="10" placeholder="please input product of JSON type "  v-model="product1" id="product1" class="textarea" required/></textarea>
          </div>
          <div >
              <h3>product2</h3>
              <textarea rows="10" placeholder="please input product of JSON type "  v-model="product2" id="product2" class="textarea" required></textarea>
          </div>
          <div >
              <button v-on:click="compareJsonProduct" style="float: right" class="primary">compare</button>
          </div>
          <div class="secret-key">
              <h3>result</h3>
              <textarea rows="10" name="message" id="message" class="textarea" v-model="message"></textarea>
              <pre id="errors" class="errors" v-if="errors">{{errors}}</pre>
          </div>
      </div>
  </div>
  `,
  data() {
    return {
      message: '',
      errors: '',
      product1: '',
      product2: '',
    };
  },
  methods: {
    compareJsonProduct() {
      const my = this;
      my.message = '';
      my.errors = '';
      axios.defaults.headers["Content-Type"] = "application/json";
      axios.post('/product/compareJson',
        {
          "jsonStr1": my.product1,
          "jsonStr2": my.product2
        }, {
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(function (response) {
        // handle success
        console.log("response", response);
        if (response.status === 200) {
          my.message = JSON.stringify(response.data);
        } else {
          my.errors = response.data;
        }
      }).catch(function (error) {
        // handle error
        console.log("error", error);
        my.errors = JSON.stringify(error, null, 4);
      }).then(function () {
        // always executed
      });
    }
  }
}
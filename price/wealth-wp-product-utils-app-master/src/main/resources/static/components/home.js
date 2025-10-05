export default {
    template: `
    <div class="container">
        <div class="header">
            <h2>Dashboard</h2>
        </div>
        <div class="upload-content">
            <div class="single-upload">
                <h3>STEP 1: Upload Configuration Package File(*.zip)</h3>
                <input id="singleFileUploadInput" type="file" name="file" class="input" required
                    v-on:change="onFileChange" />
                <br />
                <div style="padding-top: 8px">
                    <button v-on:click="uploadFile" class="primary submit-btn" v-if="file">Upload Config
                        Package</button>
                </div>
            </div>
            <div class="multiple-upload">
                <h3>STEP 2: Check extension fields against product metadata</h3>
                <button v-on:click="dataChecking" class="primary submit-btn">Perform Data Checking</button>
            </div>
            <div class="multiple-upload">
                <h3>STEP 3: Load data from OracleDB to MongoDB / DocumentDB</h3>
                <button v-on:click="dataLoading" class="primary submit-btn">Perform Data Loading</button>
            </div>
            <hr>
            <div class="multiple-upload">
                <h3>Add indexes to MongoDB / DocumentDB</h3>
                <button v-on:click="fetchDataIndexes" class="primary submit-btn">Perform Fetch Indexes</button>
            </div>
            <div>
                <pre id="errors" class="errors" v-if="errors">{{errors}}</pre>
                <pre id="message" class="message" v-if="message">{{message}}</pre>
            </div>
        </div>
    </div>
  `,
    data() {
        return {
            file: '',
            message: '',
            errors: ''
        };
    },
    methods: {
        uploadFile() {
            const my = this;
            const fileInput = document.querySelector('#singleFileUploadInput');
            const formData = new FormData();
            formData.append("file", this.file);
            my.message = '';
            my.errors = '';
            axios.post('api/uploadFile', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(function (response) {
                // handle success
                console.log("response", response);
                if (response.status === 200) {
                    my.message = response.data;
                } else {
                    my.errors = response.data;
                }
            }).catch(function (error) {
                // handle error
                console.log("error", error);
                my.errors = JSON.stringify(error, null, 4);
            }).then(function () {
                // always executed
                fileInput.value = '';
            });
        },
        dataChecking() {
            const my = this;
            my.message = '';
            my.errors = '';
            axios.get('api/dataChecking').then(function (response) {
                // handle success
                console.log("response", response);
                if (response.status === 200) {
                    my.message = response.data;
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
        },
        dataLoading() {
            const my = this;
            my.message = '';
            my.errors = '';
            axios.get('api/dataLoading').then(function (response) {
                // handle success
                console.log("response", response);
                if (response.status === 200) {
                    my.message = response.data;
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
        },
        fetchDataIndexes() {
            const my = this;
            my.message = '';
            my.errors = '';
            axios.get('api/fetchDataIndexes').then(function (response) {
                // handle success
                console.log("response", response);
                if (response.status === 200) {
                    my.message = response.data;
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
        },
        onFileChange(e) {
            const files = e.target.files || e.dataTransfer.files;
            if (!files.length)
                return;
            const f = files[0];
            console.log("file", f);
            this.message = "";
            this.errors = "";
            if (f.name.toLowerCase().endsWith(".zip")) {
                this.file = f;
            } else {
                const fileInput = document.querySelector('#singleFileUploadInput');
                fileInput.value = '';
                this.errors = "Allow only *.zip file";
            }
        }
    }
}
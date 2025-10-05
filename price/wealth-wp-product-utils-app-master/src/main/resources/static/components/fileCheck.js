
export default {
  template: `
    <div class="container">
      <div class="container_loading" id="loading" v-if="isLoading">
        <div class="lading_circle lading_circle1"></div>
        <div class="lading_circle lading_circle2"></div>
        <div class="lading_circle"></div>
      </div>
      <div class="header">
        <h2>File Management</h2>
      </div>
      <div>
          <pre id="errors" class="errors" v-if="errors">{{errors}}</pre>
          <pre id="message" class="tips" v-if="message">{{message}}</pre>
          <button @click="refresh" class="refresh">Refresh</button>
          <button @click="loadTree" class="refresh home" v-if="breadcrumbs.length > 1">Go Home</button>
      </div>
      <div class="log-content" v-if="hasPower">
        <div v-if="breadcrumbs.length > 0">
          <div class="breadcrumbs">
            <span v-for="(folder, index) in breadcrumbs" :key="index" class="breadcrumb-item">
              <span @click="navigateTo(index)" class="breadcrumb-link">{{ folder.name }}</span>
              <span v-if="index < breadcrumbs.length - 1"> / </span>
            </span>
          </div>
        </div>

        <div v-if="directoryList.length > 0" class="directory-container">
          <h4>Directory</h4>
          <ul class="directory-list" @mouseover="handleMouseOver(directoryList, $event)">
            <li v-for="(item, index) in directoryList" :key="index" :id="item.name"
             class="directory-card" @click="enterDirectory(item)">
              <div class="directory-inner">
                <img src="images/folder.png" alt="Folder" class="folder-icon" />
                <span class="folder-name" :title="item.name">{{ item.name }}</span>
              </div>
            </li>
          </ul>
        </div>

        <div v-if="fileList.length > 0" class="file-container">
          <h4>All File</h4>
          <table class="file-table">
            <thead>
              <tr>
                <th v-for="(col, index) in column" :key="index"  @click="handleSort(col.sort)">
                  <div class="table-title">
                    <span>{{col.name}}</span>
                    <div v-if="col.sort">
                        <div class="arrow arrow-up" :class="{active: isActive(col.sort, 'asc')}"/>
                        <div class="arrow arrow-down" :class="{active: isActive(col.sort, 'desc')}"/>
                    </div>
                  </div>

                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(file, index) in fileList" :key="index">
                <td class="fileName">{{ file.name }} </td>
                <td>{{ formatFileSize(file.size)}}</td>
                <td>{{ file.updateTime }}</td>
                <td>
                  <img src="images/download.png" alt="Folder" class="download-icon" @click="downloadFile(file)"/>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  data() {
    return {
      fileTree: null,
      column: [{ name: "Name", sort: "name" },
      { name: "File Size", sort: "size" },
      { name: "Last modified", sort: "updateTime" },
      { name: "Actions", sort: false }],
      currentFiles: [],
      directoryList: [],
      fileList: [],
      breadcrumbs: [],
      hasPower: false,
      message: '',
      errors: '',
      isLoading: false,
      sortKey: 'updateTime',
      sotDirection: 'desc'
    };
  },
  props: ['secretKey'],
  created() {
    if (this.secretKey) {
      this.fetchDirectory()
    } else {
      this.message = "Please Enter SecretKey"
    }
  },
  methods: {
    loadTree() {
      this.fetchDirectory();
    },
    refresh() {
      const { length } = this.breadcrumbs
      const directory = this.breadcrumbs[length - 1]
      if (directory && directory.absolutePath) {
        this.fetchDirectory(`${directory.absolutePath}`, directory.name, directory, true)
      } else {
        this.fetchDirectory();
      }
    },
    isActive(key, direction) {
      return this.sortKey === key && this.sotDirection === direction;
    },
    handleSort(key) {
      if (this.sortKey === key) {
        this.sotDirection = this.sotDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortKey = key
        this.sotDirection = 'asc'
      }
      this.updateLists(this.currentFiles);
    },
    fetchDirectory(path = null, keyName = '', directory = null, refresh = null) {
      const my = this;
      my.message = '';
      my.errors = '';
      my.isLoading = true
      axios.get("api/listFiles", {
        params: { path }
      }).then(function (response) {
        if (response.status === 200) {
          const data = response.data
          if (!path) {
            const name = data[0].parentName
            my.fileTree = {
              name: name,
              children: data
            };
            my.hasPower = true;
            my.updateLists(data);
            my.breadcrumbs = [{ name: name, children: data }]
          } else {
            const { length } = my.breadcrumbs
            my.fileTree = my.updateChildren(my.fileTree, keyName, data);
            my.currentFiles = data;
            my.updateLists(data);
            directory.children = data;
            if (refresh) {
              my.breadcrumbs[length - 1] = directory;
            } else {
              my.breadcrumbs.push(directory);
            }

          }
        } else {
          my.errors = response.data;
          my.hasPower = false;
          my.fileTree = null;
          my.breadcrumbs = [];
        }
        my.isLoading = false
      }).catch(function (error) {
        my.errors = JSON.stringify(error, null, 4);
        my.isLoading = false
      });
    },

    updateChildren(collection, targetName, newChildren) {
      function recursiveUpdate(node) {
        if (node.name === targetName) {
          node.children = newChildren;
          return true;
        }
        if (node.children && node.children.length > 0) {
          for (let child of node.children) {
            if (recursiveUpdate(child)) {
              return true;
            }
          }
        }
        return false;
      }
      const updatedCollection = JSON.parse(JSON.stringify(collection));
      recursiveUpdate(updatedCollection);
      return updatedCollection;
    },

    formatFileSize(size) {
      if (size < 1024) {
        return `${size} B`;
      } else if (size < 1024 * 1024) {
        return `${(size / 1024).toFixed(2)} KB`;
      } else {
        return `${(size / (1024 * 1024)).toFixed(2)} MB`;
      }
    },

    updateLists(children) {
      this.currentFiles = children
      this.directoryList = children.filter((item) => item.type === "directory");
      this.fileList = this.sortData(children.filter((item) => item.type === "file"), this.sortKey, this.sotDirection);
    },

    sortData(array, sortKey, sotDirection) {
      if (!Array.isArray(array) || !sotDirection) {
        return array;
      }
      return array.sort((a, b) => {
        const valueA = a[sortKey]
        const valueb = b[sortKey]
        if (typeof valueA === 'number' && typeof valueb === 'number') {
          return sotDirection === 'desc' ? valueb - valueA : valueA - valueb;
        }
        return sotDirection === 'desc' ? valueb.localeCompare(String(valueA)) : valueA.localeCompare(String(valueb));
      });

    },

    handleMouseOver(list, event) {
      const rectCache = {};
      for (const item of list) {
        const { name } = item
        const card = document.getElementById(name)
        if (card) {
          if (!rectCache[name]) {
            rectCache[name] = card.getBoundingClientRect();
          }
          const rect = rectCache[name];
          const x = event.clientX - rect.left - rect.width / 2;
          const y = event.clientY - rect.top - rect.height / 2;
          card.style.setProperty("--x", `${x}px`);
          card.style.setProperty("--y", `${y}px`);
        }
      }
    },

    enterDirectory(directory) {
      if (directory.type === "directory") {
        if (!directory.children) {
          this.fetchDirectory(`${directory.absolutePath}`, directory.name, directory)

        } else {
          this.currentFiles = directory.children;
          this.updateLists(directory.children);
          this.breadcrumbs.push(directory);
        }
      }
    },

    navigateTo(index) {
      this.breadcrumbs = this.breadcrumbs.slice(0, index + 1);
      this.currentFiles = this.breadcrumbs[index].children;
      this.updateLists(this.currentFiles);
    },

    downloadFile(file) {
      const my = this;
      my.errors = '';
      const filePath = `${file.absolutePath.replace(/\\/g, '/')}`
      const url = `api/download?filePath=${filePath}`;
      fetch(url, { headers: { "x-dummy-secret-key": this.secretKey } })
        .then(response => {
          if (response.status === 401) {
            my.errors = JSON.stringify("Secret key is invalid", null, 4);
          } else if (!response.ok) {
            throw new Error(`Failed to download file: ${response.statusText}`);
          } else {
            return response.blob();
          }
        })
        .then(blob => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement("a");
          a.style.display = "none";
          a.href = url;
          a.download = file.name;
          document.body.appendChild(a);
          a.click();
          window.URL.revokeObjectURL(url);
        })
        .catch(error => {
          console.error("Error downloading file:", error);
        });
    }
  }
}
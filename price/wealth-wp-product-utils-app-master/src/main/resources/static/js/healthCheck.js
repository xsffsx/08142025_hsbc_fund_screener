mermaid.initialize({ startOnLoad: false });
const rootServiceId = "product-utils";
var checkList = [];
var connectionList = [];

function isValidConfig(cfg) {
    if (typeof cfg !== "object" || cfg === null) {
        return false;
    }
    if (!cfg.hasOwnProperty("checkList") || !cfg.hasOwnProperty("connectionList")) {
        return false;
    }
    if (typeof cfg.connectionList !== "object" || typeof cfg.connectionList !== "object") {
        return false;
    }
    return true;
}

function sanitizeHTML(input) {
    const temp = document.createElement("div");
    temp.textContent = input;
    return temp.innerHTML;
}

function encodeSvg(svgElement) {
    var base64Data = btoa(svgElement);
    var dataUri = 'data:image/svg+xml;base64,' + base64Data;
    return dataUri
}

function renderGraph(text) {
    mermaid.render("graph", text)
        .then(function (svgCode) {
            if (typeof svgCode !== "object" || svgCode === null || svgCode.svg.includes("<script>")) {
                console.error("Error");
            } else {
                const healthCheckDiv = document.getElementById("healthCheckDiv");
                healthCheckDiv.innerHTML = "";
                const img = new Image()
                img.src = encodeSvg(svgCode.svg)
                img.style.width = '100%'
                img.style.height = '100%'
                img.style.zIndex = 99
                healthCheckDiv.appendChild(img);
            }
        })
        .catch(function (error) {
            console.error("Error rendering graph:", error);
        });
}

function fetchHealthCheckScopes() {
    return new Promise((resolve, reject) => {
        fetch("healthCheckScopes", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            }
        })
            .then(response => response.json())
            .then(cfg => {
                if (isValidConfig(cfg)) {
                    createCache(cfg);
                    renderGraph(genGraphText());
                    drawDetail()
                } else {
                    console.error("Error data");
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
    })
}

async function healthCheck() {
    const mainDOM = document.getElementById("main")
    mainDOM.style.display = 'none'
    const loading = document.getElementById("loading")
    loading.style.display = 'flex'
    const responseTimes = [];
    for (const item of checkList) {
        let time = 0
        let status = "UP"
        const { serviceId, parameters, dataType } = item

        try {
            await fetchapi(serviceId, parameters, dataType).then((res) => {
                const { time: num, status: sta } = res
                time = num.toFixed(0)
                status = sta
            })
        } catch {
            continue
        }
        responseTimes.push({
            serviceId, time, status
        })
    }
    handleTime(responseTimes)
}

function fetchapi(serviceId, parameters, dataType) {
    var body = {};
    if (parameters) {
        parameters.forEach(param => body[param.key] = param.value);
    }
    const startTime = performance.now();
    return new Promise((resolve, reject) => {
        fetch("healthCheck/" + serviceId, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
        })
            .then(response => response.json())
            .then(json => {
                const endTime = performance.now();
                updateCache(json);
                renderGraph(genGraphText());
                updateDetail(dataType, json);
                resolve({ time: endTime - startTime, status: json.status })
            })
            .catch(error => {
                console.error("Error:", error);
            });
    })

}

function updateDetail(dataType, check) {
    if (check.serviceId === "product-dp-file-egress" || check.serviceId === "product-dp-file-ingress") {
        document.getElementById("response_" + check.serviceId).textContent = formatResponse(dataType, check.response);
    } else {
        document.getElementById("request_" + check.serviceId).textContent = formatResponse(dataType, check.request);
        document.getElementById("response_" + check.serviceId).textContent = formatResponse(dataType, check.response);
    }

}

function formatXml(xmlString) {
    const parser = new DOMParser()
    const xmlDoc = parser.parseFromString(xmlString, "application/xml")
    const serializer = new XMLSerializer()
    const formattedXml = serializer.serializerToString(xmlDoc)

    const formattedXmlString = formattedXml.replace(/></g, ">\n<")
    return formattedXmlString
}

function formatResponse(dataType, value) {
    var s;
    if (typeof value === "object" && value !== null) {
        s = JSON.stringify(value, null, 4);
    } else {
        s = String(value);
    }
    s = dataType === 'XML' ? formatXml(s) : s
    return s || ""
}

function createCache(cfg) {
    cfg.checkList.forEach(check => {
        checkList.push(check);
    });
    cfg.connectionList.forEach(connection => {
        connectionList.push(connection);
    });
}

function drawLine(status) {
    var style = document.createElement('style');
    var color = "#9ab592"
    if (status === "DOWN") {
        color = "#db2a2a"
    } else {
        color = "#9ab592"
    }
    style.innerHTML = `.div_graph::before {position: absolute;
        content: "";
        width: 400%;
        height: 200%;
        background: ${color};
        z-index: 1;
        left: 50%;
        top: 50%;
        transform-origin: left top;
        animation: rotation 7s linear infinite;}`;
    document.head.appendChild(style)
}

function updateCache(check) {
    var cache = checkList.filter(c => c.serviceId == check.serviceId)[0];
    cache.status = check.status;
    cache.request = check.request;
    cache.response = check.response;
    if (check.status === "UP") {
        connectionList.forEach(connection => {
            if (connection.source === rootServiceId && connection.target === cache.serviceId) {
                connection.status = "UP";
            }
            if (connection.source === cache.serviceId) {
                connection.status = "UP";
            }
        })
    } else if (check.status === "DOWN") {
        connectionList.forEach(connection => {
            if (connection.source === rootServiceId && connection.target === cache.serviceId) {
                connection.status = "DOWN";
            }
            if (connection.source === cache.serviceId) {
                connection.status = "UNKNOW";
            }
        })
    }
}

function genGraphText() {
    var utils = { "serviceId": rootServiceId, "status": "UP" };
    var serviceIdText = [utils, ...checkList].map(check => {
        var className;
        if (check.status === "UP") {
            className = "cUp";
        } else if (check.status === "DOWN") {
            className = "cDown";
        } else {
            className = "cUnknown";
        }
        return `${check.serviceId}([${check.serviceId}]):::${className}`;
    }).join("\n");
    var connectionText = connectionList.map(connection => {
        var linkText;
        if (connection.status === "UP") {
            linkText = "===";
        } else if (connection.status === "DOWN") {
            linkText = "-.-x";
        } else {
            linkText = "-.-";
        }
        return `${connection.source} ${linkText} ${connection.target}`;
    }).join("\n");
    var text = `---
title: Health Check Graph
---
graph TD;
classDef cUp fill:#a1bf99, stroke:#a687b0,stroke-width:2px;
classDef cDown fill:#ff0000, stroke:#a687b0,stroke-width:2px;
classDef cUnknown fill:#eeeeee, stroke:#a687b0,stroke-width:2px;
${serviceIdText}
${connectionText}
`;
    return text;
}

function handleTime(list) {
    var total = 0
    list.forEach(i => {
        total = JSAdd(total, i.time)
    })
    list = list.map(i => {
        return {
            ...i,
            pct: JSMul(JSDivide(i.time, total).toFixed(2), 100)
        }
    })
    drawPerformance(list)
}

function drawPerformance(list) {
    var listBody = document.getElementById("div_list_items");
    listBody.innerHTML = ""
    let status = "UP"
    list.forEach((i, index) => {
        var { serviceId, time, pct, status: sta } = i
        if (sta === "DOWN") {
            status = sta
        }
        var listItem = document.createElement("div");
        var itemName = document.createElement("span");
        var itemTime = document.createElement("span");
        itemName.textContent = serviceId
        itemTime.textContent = `${time}ms`
        listItem.className = "div_list_item"
        var style = document.createElement('style');
        var color = "#9ab592"
        if (pct > 30) {
            color = "#e7b472"
        } else if (pct > 4) {
            color = "#a687b0"
        }
        style.innerHTML = `.div_list .div_list_items .div_list_item:nth-child(${index + 1})::before { background-color: ${color} ; width: ${pct}%}`;
        document.head.appendChild(style)
        listItem.appendChild(itemName)
        listItem.appendChild(itemTime)
        listBody.appendChild(listItem);
    })
    const mainDOM = document.getElementById("main")
    mainDOM.style.display = 'block'
    drawLine(status)
    const loading = document.getElementById("loading")
    loading.style.display = 'none'
}

function drawDetail() {
    const detailBody = document.getElementById("graph_box");
    checkList.forEach((check) => {
        const { serviceId, position, parameters = false } = check
        const leftNode = document.createElement("div")
        leftNode.className = "graph_contain_left"
        const rightNode = document.createElement("div")
        rightNode.className = "graph_contain_rigth"
        const title = document.createElement("div")
        title.textContent = serviceId
        title.className = "graph_contain_title"
        const box = document.createElement("div")
        box.className = "graph_box_info"
        const paramNode = parameters ? drawParams(parameters, serviceId) : false
        if (position === 'left') {
            const isdpFile = serviceId === "product-dp-file-egress" || serviceId === "product-dp-file-ingress"
            const reqNode = drawInfo(serviceId, "request", "400px")
            const respNode = drawInfo(serviceId, "response", isdpFile ? "100px" : "400px", parameters)
            paramNode && box.appendChild(paramNode)
            paramNode && box.appendChild(reqNode)
            box.appendChild(respNode)
            leftNode.appendChild(title)
            leftNode.appendChild(box)
            detailBody.appendChild(leftNode)
        }
        if (position === 'right') {
            const reqNode = drawInfo(serviceId, "request", "200px")
            const respNode = drawInfo(serviceId, "response", "300px", true)
            const line = document.createElement("div")
            line.className = "graph_box_line"
            line.appendChild(paramNode)
            line.appendChild(reqNode)
            box.appendChild(line)
            box.appendChild(respNode)
            rightNode.appendChild(title)
            rightNode.appendChild(box)
            detailBody.appendChild(rightNode)
        }
    })
    const loading = document.getElementById("loading")
    loading.style.display = 'none'
}


function handleFirst(str) {
    if (str.length === 0) return str
    return str.charAt(0).toUpperCase() + str.slice(1)
}

function drawInfo(serviceId, type, rows) {
    const itemNode = document.createElement("div")
    itemNode.className = `graph_box_${type}`
    const title = document.createElement("h3")
    title.textContent = handleFirst(type)
    title.className = "graph_box_sub"
    const msgContent = document.createElement("pre")
    msgContent.setAttribute('id', `${type}_${serviceId}`)
    msgContent.style.height = rows
    msgContent.className = "graph_box_pre"
    itemNode.appendChild(title)
    itemNode.appendChild(msgContent)
    return itemNode

}

function drawParams(parameters, serviceId) {
    const paramNode = document.createElement("div");
    paramNode.className = "graph_box_params"
    const itemTitle = document.createElement("h3");
    itemTitle.textContent = "Parameters"
    itemTitle.className = "graph_box_sub"
    paramNode.appendChild(itemTitle)
    parameters.forEach(param => {
        const { desc, value, key } = param
        const paramItem = document.createElement("div");
        const itemDesc = document.createElement("span");
        const itemVal = document.createElement("input");
        paramItem.className = "graph_box_param"
        itemDesc.textContent = sanitizeHTML(desc)
        itemVal.type = "text"
        itemVal.value = sanitizeHTML(value);
        itemVal.addEventListener("change", (e) => {
            const newVal = e.target.value
            itemVal.value = sanitizeHTML(e.target.value);
            updateVal(serviceId, key, newVal)
        })
        paramItem.appendChild(itemDesc)
        paramItem.appendChild(itemVal)
        paramNode.appendChild(paramItem)
    })
    return paramNode
}

function updateVal(id, keyName, newVal) {
    checkList = checkList.map((i) => {
        let { parameters, serviceId } = i
        if (serviceId === id) {
            parameters = parameters.map(param => {
                const { key } = param
                if (keyName === key) {
                    return {
                        ...param,
                        value: newVal
                    }
                } else {
                    return param
                }
            })
        }
        return { ...i, parameters }
    })
}


function JSAdd(num1, num2) {
    if (!num1 || !num2) {
        return num1 + num2
    }
    let r1, r2
    try {
        r1 = num1.toString().split('.')[1].length
    } catch (f) {
        r1 = 0
    }
    try {
        r2 = num2.toString().split('.')[1].length
    } catch (f) {
        r2 = 0
    }
    const h = Math.pow(10, Math.max(r1, r2))
    return (JSMul(num1, h) + JSMul(num2, h)) / h
}

function JSDivide(num1, num2) {
    if (!num1 || !num2) {
        return 0
    }
    let r1, r2
    try {
        r1 = num1.toString().split('.')[1].length
    } catch (f) {
        r1 = 0
    }
    try {
        r2 = num2.toString().split('.')[1].length
    } catch (f) {
        r2 = 0
    }
    return JSMul(Number(num1.toString().replace('.', '')) / Number(num2.toString().replace('.', '')), Math.pow(10, r2 - r1))
}

function JSMul(num1, num2) {
    if (!num1 || !num2) {
        return 0
    }
    let r1 = 0
    const a = num1.toString()
    const b = num2.toString()
    try {
        if (a.split('.')[1]) {
            r1 += a.split('.')[1].length
        }
    } catch (f) {
        console.log(f)
    }
    try {
        if (b.split('.')[1]) {
            r1 += b.split('.')[1].length
        }
    } catch (f) {
        console.log(f)
    }
    return (Number(a.replace('.', '')) * Number(b.replace('.', ''))) / Math.pow(10, r1)
}


fetchHealthCheckScopes();
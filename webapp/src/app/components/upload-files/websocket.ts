import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { UploadFilesComponent } from './upload-files.component';

export class WebSocketAPI {
    webSocketEndPoint: string = 'http://localhost:8080/ws';
    topic: string = "/queue/enkaizen_queue";
    stompClient: any;
    appComponent: UploadFilesComponent;
    constructor(appComponent: UploadFilesComponent) {
        this.appComponent = appComponent;
    }
    _connect() {
        console.log("Initialize WebSocket Connection");
        let ws = new SockJS(this.webSocketEndPoint);
        this.stompClient = Stomp.over(ws);
        const _this = this;
        _this.stompClient.connect({}, function (frame) {
            _this.stompClient.subscribe(_this.topic, function (sdkEvent) {
                _this.onMessageReceived(sdkEvent);
            });
        }, this.errorCallBack);
    };

    _disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    errorCallBack(error) {
        console.log("errorCallBack -> " + error)
        setTimeout(() => {
            this._connect();
        }, 5000);
    }

    _send() {
        console.log("Calling fak api to get notification");
        this.stompClient.send("/notification");
    }

    onMessageReceived(message) {
        console.log("Recieved message from Server: " + message);
        this.appComponent.handleMessage(JSON.stringify(message.body));
    }
}
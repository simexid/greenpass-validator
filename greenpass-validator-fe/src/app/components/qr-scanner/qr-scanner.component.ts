import { Component, Output, EventEmitter  } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import {Html5Qrcode} from "html5-qrcode"
import { HttpconfigService } from 'src/app/config/httpconfig.service';
import { GreenPassModel } from 'src/app/model/GreenPassModel';


@Component({
  selector: 'app-qr-scanner',
  templateUrl: './qr-scanner.component.html',
  styleUrls: ['./qr-scanner.component.less']
})

/**
 * Qr scanner component, used to retrieve data from qr-code by accessing the camera
 */
export class QrScannerComponent {

  @Output() output = new EventEmitter<GreenPassModel>();
  @Output() isScanning = new EventEmitter<Boolean>(); 

  cameras;
  multipleCamera = 1;
  selectedCamera = 0;
  reader;
  attempt = 1;
  isInit = false;
  lastQrTime: number = 0;
  data: GreenPassModel = new GreenPassModel();

  constructor(private http: HttpconfigService, private translate: TranslateService) { }

  /**
   * Method used to start aquisition
   */
  start(): void {
    Html5Qrcode.getCameras().then(devices => {
      if (devices && devices.length) {
        this.cameras = devices;
        if (devices.length > 1) {
          this.multipleCamera = devices.length;
          devices.forEach((camera, index) => {
            if (camera.label.toUpperCase().includes("BACK") 
            || camera.label.toUpperCase().includes("REAR")
            || camera.label.toUpperCase().includes("POSTERIORE")) {
              this.selectedCamera = index;
              return;
            }
          });
        }
        this.reader = new Html5Qrcode("reader");
        this.initScanner();
      }
    }).catch(err => {
      alert(this.translate.instant('ERROR.CAMERA_AUTHORIZATION'));
    });
  }

  /**
   * Init camera and scanner
   */
  initScanner() {
    this.data = new GreenPassModel();
    this.reader.start(
      this.cameras[this.selectedCamera].id,     
      {
        fps: 10,    
        qrbox: 250         
      },
      qrCodeMessage => {
        if (qrCodeMessage.search("HC1:")>-1) {
          this.reader.stop().then(ignore => {  
            this.isInit = false;
            this.isScanning.emit(false);
            // CHIAMA SERVIZIO E MOSTRA DATI
            this.http.check(qrCodeMessage).subscribe(res=>{
              this.data=res;
                this.data.isValid = true;
                this.output.emit(this.data);
            }, error => {
              this.data.isValid = false;
              this.output.emit(this.data);
              alert(this.translate.instant('ERROR.CONNECTION'))})
          }).catch(err => {
            this.isInit = false;
            this.isScanning.emit(false);
            this.output.emit(this.data);
            alert(this.translate.instant('ERROR.QR_READER'));
          });
        }else {
          let time: number = new Date().getTime();
          if (time-this.lastQrTime>5000) {
            this.data = new GreenPassModel();
            //ERRORE LETTURA
            alert(this.translate.instant('ERROR.QR_READER'));
            this.output.emit(this.data);
            this.isInit = false;
          this.lastQrTime=time;
          }
        }
      },
      errorMessage => {
        // NON LEGGO ANCORA IL QR
      })
    .catch(err => {
      if (this.attempt > this.cameras.length) {
      this.isInit = false;
      this.isScanning.emit(false);
      alert(this.translate.instant('ERROR.CAMERA_ACCESS'));
      } else {
        this.attempt = this.attempt+1;
        this.changeCamera();
      }
      this.data = new GreenPassModel();
      this.output.emit(this.data);
    });
    this.isInit = true;
    this.isScanning.emit(true);
  }

  /**
   * 
   * Change the camera if multiple recognized
   */
  changeCamera() {
    if (this.multipleCamera === 1) {
      return;
    }
    if (this.selectedCamera === (this.multipleCamera-1)) {
      this.selectedCamera = 0;
    } else {
      this.selectedCamera = this.selectedCamera+1;
    }
    this.reader.stop().then(ignore => {
      this.initScanner();
    }).catch(err => {
      this.initScanner();
    });
  }

  /**
   * Stop aquisition
   */
  stopCamera() {
    this.reader.stop().then(ignore => {
    }).catch(err => {
    });
    this.isInit = false;
    this.isScanning.emit(false);
    this.data = new GreenPassModel();
      this.output.emit(this.data);
    this.attempt=1;
  }

}

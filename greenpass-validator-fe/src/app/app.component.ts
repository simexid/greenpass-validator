import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { GreenPassModel } from './model/GreenPassModel';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})

/**
 * Main calss
 */
export class AppComponent {
  data: GreenPassModel;
  scanActive: Boolean = false;

  /**
   * 
   * @param translate for translation support. Create your custom language under assets/i18n
   */
  constructor(translate: TranslateService) {
    /**
     * Default language
     */
    translate.setDefaultLang('it');

    /**
     * Language to use
     */
    translate.use('it');
}

  /**
   * 
   * @param event Greenpass data received by qr-scanner component
   */
  updateData(event: GreenPassModel) {
    this.data = event;
  }

  /**
   * 
   * @param event indicates if camera is active
   */
  inScanning(event: Boolean) {
    this.scanActive = event;
  }
}

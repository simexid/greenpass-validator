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
   * Cnstructor
   * @param translate for translation support. Create your custom language under assets/i18n
   */
  constructor(private translate: TranslateService) {

    /**
     * Identify language. Set to it if italian recognized otherwise set to english.
     */
    const userLang = navigator.language;
    switch (userLang) {
      case 'it-IT':
      case 'IT':
      case 'it':
        this.setLanguage('it');
        break;
      default:
        this.setLanguage('en');
    }

}

  /**
   * Update the data in page
   * @param event Greenpass data received by qr-scanner component
   */
  updateData(event: GreenPassModel) {
    this.data = event;
  }

  /**
   * Method user to retrieve the event for camere running
   * @param event indicates if camera is active
   */
  inScanning(event: Boolean) {
    this.scanActive = event;
  }

  /**
   * Method used to set the language
   * @param lang language to set
   */
  setLanguage(lang: string) {
    this.translate.setDefaultLang('lang');
    this.translate.use('lang');
  }
}

import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { GreenPassModel } from 'src/app/model/GreenPassModel';

@Component({
  selector: 'app-show-data',
  templateUrl: './show-data.component.html',
  styleUrls: ['./show-data.component.less']
})

/**
 * Component used to show data returned by qr-scanner
 */
export class ShowDataComponent implements OnInit, OnChanges{
  @Input() data: GreenPassModel;
  stato: string;
  constructor() { }
  ngOnInit(): void {
    this.checkStato();
  }

  /**
   * method to check if the data is valid or not
   */
  checkStato(): void {
    if (this.data!=null && this.data.isValid !== undefined) {
      this.data.isValid ? this.stato = 'valid' : this.stato = 'invalid';
    } else {
      this.stato = 'undefined';
    }
  }

  ngOnChanges(changes: SimpleChanges) {  
    this.checkStato();
  }
  
}

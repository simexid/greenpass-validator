import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { GreenPassModel } from '../model/GreenPassModel';

@Injectable({
  providedIn: 'root'
})
export class HttpconfigService {

  constructor(private http: HttpClient) { }

  check(code: String): Observable<GreenPassModel> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
      })
    };
    const host = window.location.protocol + "//" + window.location.host;
    return this.http.post<any>(host+"/api/checkCertificate", {code: code}, httpOptions);
  }
}

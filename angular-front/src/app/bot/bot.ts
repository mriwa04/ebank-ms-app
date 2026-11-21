import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AsyncPipe } from '@angular/common';
import { MarkdownComponent } from 'ngx-markdown';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-bot',
  imports: [FormsModule, AsyncPipe, MarkdownComponent],
  templateUrl: './bot.html',
  styleUrl: './bot.css'
})
export class Bot {
  private http = inject(HttpClient);
  query: any = '';
  response$!: Observable<string>;

  askAgent() {
    const url = 'http://localhost:9999/EBANK-BOT/chat?query=' + encodeURIComponent(this.query);
    this.response$ = this.http.get(url, { responseType: 'text' });
  }
}

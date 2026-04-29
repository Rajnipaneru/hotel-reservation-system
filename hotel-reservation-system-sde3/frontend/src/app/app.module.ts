import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { BookingPanelComponent } from './components/booking-panel/booking-panel.component';
import { BuildingVisualizationComponent } from './components/building-visualization/building-visualization.component';

@NgModule({
  declarations: [
    AppComponent,
    BookingPanelComponent,
    BuildingVisualizationComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
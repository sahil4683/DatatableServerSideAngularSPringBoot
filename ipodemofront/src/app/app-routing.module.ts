import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { DataTableComponent } from './data-table/data-table.component';
import { ShowDataComponent } from './show-data/show-data.component';

const routes: Routes = [

  // { path: '',   redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: ShowDataComponent },
  { path: 'table', component: DataTableComponent },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

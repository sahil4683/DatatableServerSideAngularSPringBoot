import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-show-data',
  templateUrl: './show-data.component.html',
  styleUrls: ['./show-data.component.css']
})
export class ShowDataComponent implements OnInit {

  constructor(private route: ActivatedRoute) { }

  queryParamsData = {};

  ngOnInit(): void {

    this.route.queryParams.subscribe(data => {
      console.log(data);
      console.log(data['name']);
      this.queryParamsData = data;

    });
  }

}

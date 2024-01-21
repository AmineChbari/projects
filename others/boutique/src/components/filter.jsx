import React from 'react';
import "../assets/style/productList.css";

/*
 define root component
*/
export default class Filter extends React.Component {
  constructor(props) {
    super(props);
    this.handlefilterChange = this.handlefilterChange.bind(this);
  }


  render() {
    return (
        <input
            className='filter' id="filtre" type="text" placeholder="filtre products..."
            value = {this.props.filterText}
            onChange = {this.handlefilterChange}
        />
    );
  }
  handlefilterChange(event){
    this.props.filterChanged(event.target.value)
  }
  

}

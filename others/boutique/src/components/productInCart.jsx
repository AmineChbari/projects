import React from 'react';
import "../assets/style/product.css";
import Poubelle from "../assets/images/poubelle.jpg";
/*
 define root component
*/
export default class ProductInCart extends React.Component {
  constructor(props) {
    super(props);
    this.state = {selectedQuantity: 1};
    this.handleQuantityChange = this.handleQuantityChange.bind(this);
  }

  render() {
    const stockOptions = [];
    for (let i = 1; i <= this.props.originStock; i++) {
      stockOptions.push(
        <option value={i} key={i}>
          {i}
        </option>
      );
    }

    return (
      <div className='product'>
        <div className='info'>
            <div className='name'>{this.props.name}</div>
        </div>
        <div className='imageProduit'>
                <img src={this.props.image} alt={this.props.name} />
        </div>
        <select name="nbProducts" value={this.state.selectedQuantity} onChange={this.handleQuantityChange}>
            {stockOptions}
        </select>
        <img 
          src={Poubelle} 
          alt="poubelle" 
          className='button'
          onClick = {this.handlePoubelle}
          />
      </div>
    );
  }

  handleQuantityChange = (event) => {
    const selectedQuantity = parseInt(event.target.value);
    this.setState({
      selectedQuantity: selectedQuantity
    });
    this.props.updateQte(this.props, selectedQuantity);
  };
  
  handlePoubelle = () => {
    const product = {
      id: this.props.id,
      weight: this.props.weight,
      price: this.props.price,
      xstock: this.state.selectedQuantity,
      key:this.props.id
    };
    this.props.DeleteFromCart(product);
  };
}

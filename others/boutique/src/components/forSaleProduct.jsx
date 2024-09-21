import React from 'react';
import "../assets/style/product.css";
import Panier from "../assets/images/panier.jpg";
/*
 define root component
*/
export default class ForSaleProduct extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <div className='product'>
        <div className='info'>
            <div className='name'>{this.props.name}</div>
            <div className='description'>{this.props.description}</div>
            <div className='weight'>{this.props.weight}</div>
        </div>
        <div className='imageProduit'>
            <img src={this.props.image} alt={this.props.name} />
        </div>
        <div className='stock'>qty {this.props.stock}</div>
        <div className='price'>{this.props.price}</div>
        <img 
          src={Panier} 
          alt="panier" 
          className='button'
          onClick={this.handlePanier}
        />
      </div>
    );
  }

  handlePanier = () => {
    const product = {
      id: this.props.id,
      name: this.props.name,
      weight: this.props.weight,
      image: this.props.image,
      price: this.props.price,
      stock: this.props.stock
    };
    this.props.addToCart(product);
  };
}

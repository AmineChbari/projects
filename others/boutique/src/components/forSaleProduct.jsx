import React from 'react';
import "../assets/style/product.css";
import Panier from "../assets/images/panier.jpg";

export default class ForSaleProduct extends React.Component {
  render() {
    const { stock, added } = this.props;
    const stockClass = stock > 3 ? 'stock-ok' : stock > 1 ? 'stock-low' : 'stock-last';
    const stockLabel = stock > 3
      ? `${stock} en stock`
      : stock === 1
        ? 'Dernier !'
        : `Plus que ${stock} !`;

    return (
      <div className='product'>
        <div className='imageProduit'>
          <img src={this.props.image} alt={this.props.name} />
        </div>
        <div className='info'>
          <div className='name'>{this.props.name}</div>
          <div className='description'>{this.props.description}</div>
          <div className='weight'>{this.props.weight}</div>
        </div>
        <div className={`stock ${stockClass}`}>{stockLabel}</div>
        <div className='price'>{this.props.price}</div>
        <img
          src={Panier}
          alt={added ? "Déjà au panier" : "Ajouter au panier"}
          title={added ? "Déjà dans votre panier" : "Ajouter au panier"}
          className={`button${added ? ' in-cart' : ''}`}
          onClick={this.handlePanier}
        />
      </div>
    );
  }

  handlePanier = () => {
    if (this.props.added) return;
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

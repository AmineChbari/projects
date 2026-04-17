import React from 'react';
import "../assets/style/cart.css";
import ProductInCart from './productInCart.jsx';

export default class ShoppingCart extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      products: [],
      totalWeight: 0,
      totalPrice: 0
    };
    this.DeleteFromCart = this.DeleteFromCart.bind(this);
    this.productAdded = this.productAdded.bind(this);
    this.updateQte = this.updateQte.bind(this);
  }

  componentDidUpdate(prevProps) {
    if (prevProps.sProduct !== this.props.sProduct) {
      this.productAdded(this.props.sProduct);
    }
  }

  render() {
    const { products, totalWeight, totalPrice } = this.state;

    const lst = products.map(element => (
      <ProductInCart
        key={element.id}
        id={element.id}
        name={element.name}
        description={element.description}
        weight={element.weight}
        image={element.image}
        price={element.price}
        originStock={element.stock}
        xstock={element.stock}
        DeleteFromCart={this.DeleteFromCart}
        updateQte={this.updateQte}
      />
    ));

    return (
      <div className='cart'>
        <h4>Mon Panier {products.length > 0 && `(${products.length})`}</h4>
        <div className='cartZone'>
          {lst.length === 0
            ? <div className='empty-cart'>Votre panier est vide</div>
            : lst
          }
        </div>
        {products.length > 0 && (
          <div className='cart-total'>
            <div className='total-line'>
              <span>Poids total</span>
              <span className='weight-total'>{totalWeight}</span>
            </div>
            <div className='total-line total-price-line'>
              <span>Total</span>
              <span className='price'>{totalPrice}</span>
            </div>
          </div>
        )}
      </div>
    );
  }

  DeleteFromCart(Prd) {
    this.props.productToDelete(Prd);
    this.setState(prevState => ({
      products: prevState.products.filter(item => item.id !== Prd.id),
      totalPrice: prevState.totalPrice - (Prd.price * Prd.xstock),
      totalWeight: prevState.totalWeight - (Prd.weight * Prd.xstock)
    }));
  }

  productAdded(Prd) {
    this.setState(prevState => ({
      products: [...prevState.products, Prd],
      totalPrice: prevState.totalPrice + Prd.price,
      totalWeight: prevState.totalWeight + Prd.weight
    }));
  }

  updateQte(theProps, theQte) {
    const updatedProducts = this.state.products.map(item => {
      if (item.id === theProps.id) {
        return { ...item, xstock: theQte };
      }
      return { ...item, xstock: item.stock };
    });
    this.setState(
      { products: updatedProducts },
      () => {
        this.calculateTotals(updatedProducts);
        this.props.changeQty(theProps.id, theQte);
      }
    );
  }

  calculateTotals(products) {
    let totalWeight = 0;
    let totalPrice = 0;
    products.forEach(product => {
      totalWeight += product.weight * product.xstock;
      totalPrice += product.price * product.xstock;
    });
    this.setState({ totalWeight, totalPrice });
  }
}

import React from 'react';
import "../assets/style/cart.css";
import ProductInCart from './productInCart.jsx';

/*
 define root component
*/
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
    const lst = this.state.products.map(element => (
      <ProductInCart
        id={element.id}
        name={element.name}
        description={element.description}
        weight={element.weight}
        image={element.image}
        price={element.price}
        originStock={element.stock}
        xstock={element.stock}
        DeleteFromCart={this.DeleteFromCart}
        productAdded={this.productAdded}
        updateQte={this.updateQte}
        key={element.id}
      />
    ));

    return (
      <div className='cart'>
        <div className='weight'>total weight: {this.state.totalWeight}</div>
        <h4>Cart</h4>
        {lst}
        <div className='total'>
          Total price:
          <div className='price'>{this.state.totalPrice}</div>
        </div>
      </div>
    );
  }

  // send from cart
  DeleteFromCart(Prd) {
    this.props.productToDelete(Prd);
    const updatedProducts = this.state.products.filter(item => item.id !== Prd.id);
    const prix = this.state.totalPrice - (Prd.price* Prd.xstock);
    const poids = this.state.totalWeight - (Prd.weight* Prd.xstock);
    this.setState(
      {
        products: updatedProducts,
        totalPrice:prix,
        totalWeight: poids
      });
      
  }

  // receive from shelf
  productAdded(Prd) {
    const updatedProducts = [...this.state.products, Prd];
    const prix = this.state.totalPrice + Prd.price;
    const poids = this.state.totalWeight + Prd.weight;
    this.setState(
      {
        products: updatedProducts,
        totalPrice:prix,
        totalWeight: poids
      });
        
  }

  updateQte(theProps, theQte) {
    const s = { ...theProps };
    const updatedProducts = this.state.products.map(item => {
      if (item.id === s.id) {
        const x = { ...item, xstock: theQte };
        return x;
      }
      const y = { ...item, xstock: item.stock };
      return y;
    });
    this.setState(
      {
        products: updatedProducts
      },
      () => {
        this.calculateTotals(updatedProducts);
        this.props.changeQty(s.id, theQte);
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
    this.setState({
      totalWeight: totalWeight,
      totalPrice: totalPrice
    });
  }
}
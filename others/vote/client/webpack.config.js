import path from 'path';
import { fileURLToPath } from 'url';
import HtmlWebpackPlugin from 'html-webpack-plugin';
import CopyWebpackPlugin from 'copy-webpack-plugin';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const PRODUCTION = true;

export default {
    mode: PRODUCTION ? 'production' : 'development',
    entry: {
        admin: path.resolve(__dirname, './scripts/admin.js'),
        voter: path.resolve(__dirname, './scripts/voter.js')
    },
    output: {
        path: path.resolve(__dirname, '../server/public'),
        filename: 'scripts/[name].bundle.js',
        clean: true
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.(png|jpe?g|gif|svg)$/i,
                use: [
                    {
                        loader: 'file-loader',
                        options: {
                            name: '[path][name].[ext]',
                        },
                    },
                ],
            },
            {
                test: /\.(js|jsx)$/i,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-react']
                    }
                }
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, './admin.html'),
            filename: 'admin.html',
            chunks: ['admin']
        }),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, './voter.html'),
            filename: 'voter.html',
            chunks: ['voter']
        }),
        new CopyWebpackPlugin({
            patterns: [
                    { 
                        from: 'html',
                        to: 'html'
                    },
                    { 
                        from: 'style',
                        to: 'style'
                    },
                    {
                        from: 'images',
                        to: 'images'
                    }    
            ]
        })
    ],
    mode: (PRODUCTION ? 'production' : 'development'),
    devtool: PRODUCTION ? undefined : 'eval-source-map'
}
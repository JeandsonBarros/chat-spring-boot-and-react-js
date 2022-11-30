import { createTheme, NextUIProvider } from '@nextui-org/react';
import React, { useState } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import "./App.css";
import Login from './components/screens/auth/Login';
import Header from './components/layouts/Header';
import Footer from './components/layouts/Footer';
import Container from './components/screens/Container';
import Messages from './components/screens/chat/Messages';
import Register from './components/screens/auth/Register';

const lightTheme = createTheme({
  type: 'light',
  theme: {
    colors: {
      // brand colors
      primaryLight: '$blue200',
      primaryLightHover: '$blue300',
      primaryLightActive: '$blue400',
      primaryLightContrast: '$blue500',
      primary: '#03A696',
      secondary: '#F28705',
      primaryBorder: '$blue600',
      primaryBorderHover: '#06B7DB',
      primarySolidHover: '$blue700',
      primarySolidContrast: '$white',
      primaryShadow: '#03A696',
      secondaryShadow: '#F28705',

      background: '#fff',

      gradient: 'linear-gradient(112deg, $blue100 -25%, $pink500 -10%, $purple500 80%)',
      link: '#000',

      // you can also create your own color
      myColor: '#ff4ecd'

      // ...  more colors
    },
    space: {},
    fonts: {}
  }

})

const darkTheme = createTheme({
  type: "dark", // it could be "light" or "dark"
  theme: {
    colors: {
      // brand colors
      primaryLight: '$blue200',
      primaryLightHover: '$blue300',
      primaryLightActive: '$blue400',
      primaryLightContrast: '$blue600',
      primary: '#03A696',
      secondary: '#F28705',
      primaryBorder: '$blue600n500',
      primaryBorderHover: '$$blue600600',
      primarySolidHover: '$$blue600700',
      primarySolidContrast: '$white',
      primaryShadow: '#03A696',
      secondaryShadow: '#F28705',

      background: '#012E40',

      gradient: 'linear-gradient(112deg, $blue100 -25%, $pink500 -10%, $purple500 80%)',
      link: '#fff',

      // you can also create your own color
      myColor: '#ff4ecd'

      // ...  more colors
    },
    space: {},
    fonts: {},

  }
})

/* Color Theme Swatches in Hex */
/* 
{ color: #012E40; }
{ color: #025159; }
{ color: #038C8C; }
{ color: #03A696; }
{ color: #F28705; } 
 */

export default function App() {

  const darkThemeMq = window.matchMedia("(prefers-color-scheme: dark)");
  const [isDark, setIsDark] = useState(darkThemeMq.matches)

  return (

    <NextUIProvider theme={isDark ? darkTheme : lightTheme}>

      <BrowserRouter>

        <Header isDark={isDark} setIsDark={setIsDark} />
        <hr />

        <Container>

          <Routes>

            <Route path='*' element={<h1> 404 - Page not found</h1>} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/" element={<Messages />} />

          </Routes>

        </Container>

        <hr />
        <Footer />

      </BrowserRouter>

    </NextUIProvider>

  )

}
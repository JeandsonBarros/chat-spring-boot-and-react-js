import { BsFillChatLeftDotsFill, BsFillMoonStarsFill, BsSunFill } from "react-icons/bs";
import { Dropdown, Row, Switch, Text, User } from '@nextui-org/react';
import './LayoutsStyles.css';
import { Link, useLocation, useNavigate } from "react-router-dom";
import { getToken, removeToken } from '../../services/TokenService'
import { useEffect, useState } from "react";
import Account from "../screens/auth/Account";
import Security from "../screens/auth/Security";
import { getUserData } from "../../services/AuthService";

function Header({ setIsDark, isDark }) {

    const navigation = useNavigate()
    const location = useLocation()
    const [visibleAccount, setVisibleAccount] = useState(false)
    const [visibleSecurity, setVisibleSecurity] = useState(false)
    const [email, setEmail] = useState('loanding...')

    useEffect(() => {
        if (getToken()) {
            getUserData().then(data => setEmail(data.email))
        }
    }, [location.pathname])

    const navBar = () => {

        if (location.pathname === '/login' || location.pathname === '/register') {
            return <div></div>
        }

        return (
            <Dropdown placement="bottom-left">

                <Dropdown.Button light>
                    <User
                        src="https://i.pravatar.cc/150?u=a042581f4e29026704d"
                        bordered
                    />
                </Dropdown.Button>

                <Dropdown.Menu
                    color="primary"
                    aria-label="User Actions"
                    onAction={(value) => {
                        console.log(value);

                        if (value === 'account') {
                            setVisibleAccount(true)
                        }

                        if (value === 'security') {
                            setVisibleSecurity(true)
                        }

                        if (value === 'logout') {
                            removeToken();
                            navigation('/login')
                        }

                    }}
                >

                    <Dropdown.Item key="account" css={{ height: "$18" }}>
                        <Text b color="inherit" css={{ d: "flex" }}>
                            Account data
                        </Text>
                        <Text b color="inherit" css={{ d: "flex" }}>
                            {email}
                        </Text>
                    </Dropdown.Item>

                    <Dropdown.Item key="security" withDivider>
                        Security
                    </Dropdown.Item>

                    <Dropdown.Item key="logout" color="error" withDivider>
                        Log Out
                    </Dropdown.Item>

                </Dropdown.Menu>

            </Dropdown>
        )

    }

    return (
        <>
            <header>

                <div>
                    <Link style={{ fontSize: 30 }} to="/">
                        <Row justify="center" align="center">
                            <BsFillChatLeftDotsFill style={{ marginRight: 5 }} />
                            Chat
                        </Row>
                    </Link>
                </div>

                <div>
                    <Row>
                        {navBar()}

                        <Switch
                            checked={!isDark}
                            size="xl"
                            iconOn={<BsSunFill />}
                            iconOff={<BsFillMoonStarsFill />}
                            onChange={() => {
                                setIsDark(!isDark)
                            }}
                        />
                    </Row>
                </div>

            </header>

            <Account
                visible={visibleAccount}
                setVisible={setVisibleAccount}
            />

            <Security
                visible={visibleSecurity}
                setVisible={setVisibleSecurity}
            />

        </>

    );
}

export default Header;
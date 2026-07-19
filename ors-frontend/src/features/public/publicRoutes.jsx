import { PublicLayout } from '../../layouts/PublicLayout.jsx';
import { HomePage } from './HomePage.jsx';
import { HomeRecruiter } from './HomeRecruiter.jsx';
import { AboutPage } from './AboutPage.jsx';
import { ContactPage } from './ContactPage.jsx';
import { PublicRecruiterLayout } from '../../layouts/PublicRecruiterLayout.jsx';

export const publicRoutes = [
  {
    path: '/',
    element: <PublicLayout />,
    children: [
      { index: true, element: <HomePage /> },
      { path: 'home_recruiter', element: <HomeRecruiter /> },
      { path: 'about', element: <AboutPage /> },
      { path: 'contact', element: <ContactPage /> },
    ],
  },
  {
    path: '/public_recruiter',
    element: <PublicRecruiterLayout />,
    children: [
      { index: true, element: <HomeRecruiter /> },
    ],
  }
];

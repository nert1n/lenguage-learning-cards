import { Layout } from "@app/layout";
import { authRoutes } from "@app/routes/routing/auth-router";
import { mainRoutes } from "@app/routes/routing/main-router";
import { swiperRoutes } from "@app/routes/routing/swiper-router";
import { NotFoundPage } from "@pages/not-found";
import { StartPage } from "@pages/start";

export const routes = [
	{
		path: "/",
		element: <Layout />,
		children: [
			{
				path: "*",
				element: <NotFoundPage />,
			},
			{
				path: "",
				element: <StartPage />,
			},
			...swiperRoutes,
			...authRoutes,
			...mainRoutes,
		],
	},
];

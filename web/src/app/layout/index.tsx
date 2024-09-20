import { Suspense } from "react";
import { Outlet } from "react-router-dom";

import { Loader } from "@shared/lib/ui/components/loader";

import { AuthLayout } from "./auth-layout";
import { MainLayout } from "./main-layout";

const Layout = () => {
	return (
		<main>
			<Suspense fallback={<Loader />}>
				<Outlet />
			</Suspense>
		</main>
	);
};

export { Layout, AuthLayout, MainLayout };

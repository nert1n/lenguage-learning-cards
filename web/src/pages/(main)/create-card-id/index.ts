import { lazy } from "react";

export const CreateCardIdPage = lazy(() =>
	import("./ui/create-card-id").then(module => ({
		default: module.CreateCardId,
	}))
);

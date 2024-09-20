import { MainLayout } from "@app/layout";
import { CategoriesIdPage } from "@pages/(main)/[categories]/categories-id";
import { CreateCategoryPage } from "@pages/(main)/[categories]/create-category";
import { EditCategoryIdPage } from "@pages/(main)/[categories]/edit-category-id";
import { EditInCategoryIdPage } from "@pages/(main)/[categories]/edit-in-category-id";
import { CreateCardIdPage } from "@pages/(main)/create-card-id";
import { DictionaryPage } from "@pages/(main)/dicrionary";
import { HomePage } from "@pages/(main)/home";
import { MyCardsPage } from "@pages/(main)/my-cards";
import { PhrasesPage } from "@pages/(main)/phrases";
import { ProfilePage } from "@pages/(main)/profile";
import { SettingsPage } from "@pages/(main)/settings";
import { TrainingIdPage } from "@pages/(main)/training-id";

export const mainRoutes = [
	{
		element: <MainLayout />,
		children: [
			{
				path: "categories",
				children: [
					{
						path: ":id",
						element: <CategoriesIdPage />,
					},
					{
						path: "create-category",
						element: <CreateCategoryPage />,
					},
					{
						path: "edit-category:id",
						element: <EditCategoryIdPage />,
					},
					{
						path: "edit-in-category:id",
						element: <EditInCategoryIdPage />,
					},
				],
			},
			{
				path: "create-card:id",
				element: <CreateCardIdPage />,
			},
			{
				path: "dictionary",
				element: <DictionaryPage />,
			},
			{
				path: "home",
				element: <HomePage />,
			},
			{
				path: "my-cards",
				element: <MyCardsPage />,
			},
			{
				path: "phrases",
				element: <PhrasesPage />,
			},
			{
				path: "profile",
				element: <ProfilePage />,
			},
			{
				path: "settings",
				element: <SettingsPage />,
			},
			{
				path: "training:id",
				element: <TrainingIdPage />,
			},
		],
	},
];
